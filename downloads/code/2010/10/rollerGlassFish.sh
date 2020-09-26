#!/bin/sh

TARGET=/opt/roller
TARGET_TMP=${TARGET}/tmp
UPLOADS=$TARGET/uploads
THEMES=$TARGET/themes
PLANET_CACHE=$TARGET/planetcache
SEARCH_INDEX=$TARGET/searchindex
KEY1="coffeeCrewSetupKey1"
KEY2="coffeeCrewSetupKey2"
#ROLLER_FILENAME="apache-roller-4.0.1-snapshot-20080211.tar.gz"
#ROLLER_FILENAME="apache-roller-4.0.tar.gz"
ROLLER_FILENAME="apache-roller-4.1-snapshot-m1.tar.gz"
CTX_ROOT="blogs"
RUN_AS_USER="roller"
RUN_AS_GROUP="roller"

usage() 
{
    echo "Usage: $0 <Key1> <Key2> <runAs-User> <runAs-Group>"
    echo "Example: $0 LongKey LongKey nobody nobody"
    echo "Get random keys from https://www.grc.com/passwords.htm"
    exit 1
}

if [ -z $JAVA_HOME ]
then
    JAVA_HOME=/usr/lib/jvm/java-1.5.0-sun
    echo "You did not set JAVA_HOME, trying default: $JAVA_HOME"
fi

if [ ! -d "$JAVA_HOME" ]
then
    echo "Cannot find JAVA_HOME, please modify script to point to the correct place ..."
    usage
fi

if [ -z $1 ]
then 
    usage
fi

if [ -z $2 ]
then 
    usage
fi

if [ -z $3 ]
then
    usage
fi

if [ -z $4 ]
then
    usage
fi

#creates dir if neccessary
createDir()
{
    if [ ! -d $1 ]
    then 
	mkdir $1
	chown -R $RUN_AS_USER:$RUN_AS_GROUP $1
    fi
}

fixPerms() 
{
    echo "Fixing permissions to $RUN_AS_USER:$RUN_AS_GROUP on $TARGET"
    chown -R $RUN_AS_USER:$RUN_AS_GROUP $TARGET
}


#make dirs
createDir $TARGET
createDir $UPLOADS
createDir $THEMES
createDir $PLANET_CACHE
createDir $SEARCH_INDEX

KEY1="${1}"
KEY2="${2}"
RUN_AS_USER="${3}"
RUN_AS_GROUP="${4}"

echo "Welcome to the roller installer ..."
createDir $TARGET_TMP

cd $TARGET_TMP

#Check if files already exist
if [ ! -f glassfish-installer-v2ur2-b04-linux.jar ]
then
    wget http://java.net/download/javaee5/v2ur2/promoted/Linux/glassfish-installer-v2ur2-b04-linux.jar

    if [ "$?" -ne 0 ]
    then
	echo "Error downloading Glassfish"
	exit 1
    fi
fi

if [ ! -f ${ROLLER_FILENAME} ]
then
    wget http://people.apache.org/~snoopdave/snapshots/${ROLLER_FILENAME}
    if [ "$?" -ne 0 ]
    then
	echo "Error downloading ${ROLLER_FILENAME}"
	exit 1
    fi
fi

export JAVA_HOME
java -Xmx256m -jar glassfish-installer-v2ur2-b04-linux.jar

if [ -d ../glassfish ]
then
    cd ../glassfish
    ./bin/asadmin stop-domain roller
    echo "Kicking old glassfish dir ..."
    cd ..
    rm -rf glassfish
fi

cd $TARGET_TMP

mv glassfish ..
cd ../glassfish/
chmod -R +x lib/ant/bin
lib/ant/bin/ant -f setup.xml -Ddomain.name=roller
./bin/asadmin start-domain roller

# JDBC stuff
./bin/asadmin create-jdbc-connection-pool \
--datasourceclassname org.apache.derby.jdbc.EmbeddedDataSource \
--property databaseName=\$\{com.sun.aas.instanceRoot\}/databases/rollerdb:\
connectionAttributes=\;create\\=true rollerpool

./bin/asadmin ping-connection-pool rollerpool
./bin/asadmin create-jdbc-resource --connectionpoolid=rollerpool jdbc/rollerdb

# Create mail resource
# Seems not to work with roller
#./bin/asadmin create-javamail-resource --mailhost localhost --mailuser rollermail --fromaddress roller\@blogs\.coffeecrew\.org mail/Session

./bin/asadmin delete-http-listener http-listener-1
./bin/asadmin delete-http-listener http-listener-2
./bin/asadmin create-http-listener --listeneraddress 127.0.0.1 --listenerport 8080 --acceptorthreads 32 --enabled=true --defaultvs server --securityenabled=false roller-listener

./bin/asadmin set server.http-service.http-listener.admin-listener.address=127.0.0.1
# Not needed, will be proxied
#./bin/asadmin set server.http-service.http-listener.admin-listener.server-name=$ADMIN_SERVER_NAME

# Disable IIOP stuff to listen globally, we do not need that right now.
./bin/asadmin set server.iiop-service.iiop-listener.SSL.address=127.0.0.1
./bin/asadmin set server.iiop-service.iiop-listener.SSL_MUTUALAUTH.address=127.0.0.1
./bin/asadmin set server.iiop-service.iiop-listener.orb-listener-1.address=127.0.0.1


#Disable JMX connector for remote access
./bin/asadmin set server.admin-service.jmx-connector.system.enabled=false

#JMS
./bin/asadmin set server.jms-service.jms-host.default_JMS_host.host=localhost

#Require client authentication, just to be sure ...
./bin/asadmin set server.iiop-service.client-authentication-required=true


#Restart gf to make changes effective
./bin/asadmin stop-domain roller 

fixPerms

#Start as given user
su -c "./bin/asadmin start-domain roller" $RUN_AS_USER

#Roller setup
cd $TARGET_TMP
tar zxvf $ROLLER_FILENAME

#Copy themes
cd apache-roller*/webapp/roller/themes
cp -vR * $THEMES

#Replace keys
cd ../WEB-INF
cp security.xml /tmp
cat /tmp/security.xml | sed "s/name=\"key\" value=\"anonymous\"/name=\"key\" value=\"${KEY1}\"/" | sed "s/name=\"key\" value=\"rollerlovesacegi\"/name=\"key\" value=\"${KEY2}\"/" > security.xml
#Pack war file
cd ..
jar cvf ../../../roller.war *

#Build roller-custom.properties
cat <<EOF > $TARGET/glassfish/domains/roller/lib/classes/roller-custom.properties
installation.type=auto

#Should work with JNDI but maybe not with glassfish
mail.configurationType=properties
mail.hostname=localhost

planet.aggregator.enabled=true
uploads.dir=$UPLOADS
themes.dir=$THEMES
search.index.dir=$SEARCH_INDEX
planet.aggregator.cache.dir=$PLANET_CACHE
EOF

#Deploy application
cd $TARGET/glassfish
./bin/asadmin deploy --contextroot $CTX_ROOT ../tmp/roller.war

fixPerms

echo "Finished, visit http://localhost:8080/blogs now ..."
echo
echo "After you've finished configuring your roller instance,"
echo "be sure to set the installation type to manual in"
echo $TARGET/glassfish/domains/roller/lib/classes/roller-custom.properties
echo "Have a nice day ..."
