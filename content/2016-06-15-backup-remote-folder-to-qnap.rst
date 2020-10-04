Back To QNAP
############

:date: 2016-06-15T22:36:12+01:00
:tags: Shell, Bash, Backup, QNAP
:category: Infrastructure
:author: Jens Frey
:summary: How to backup a remote folder to your QNAP storage.

Sometimes you may want to backup/synchronize data from a remote server to your local QNAP server. This can be done fairly easily and efficiently using the already installed :code:`rsync` command.

.. note::  You need to have set up a passwordless SSH login from your QNAP to your remote host for the below script to work.

In addition the script below will send an eMail once it finishes it's run. To get the script going on your machine, simply adapt the variables to point to your local directories and supply a valid eMail address (can be the same for FROM and TO).

As any operating system upgrade will otherwise delete your backup script you need to place it onto the share disk itself, as this will prevent the script of being deleted during an upgrade operation (I suggest stuffing it into a Gist on Github as well ;)).

I suggest putting the script in the following location if you have set up a RAID 5 array (the location may be slightly different for you setup) :code:`/share/MD0_DATA`.

.. code-block:: bash

   #!/bin/sh

   # Script to periodically synchronize a remote directory with your
   # local QNAP storage.

   # Source folders/user on remote machine
   BACKUP_SOURCE_USER="backup-user"
   BACKUP_SOURCE_HOST="backuphost.example.com"
   BACKUP_SOURCE_FOLDER="/backup/subdir"

   # Directory to save the remote backup locally
   BACKUP_SINK_FOLDER="/share/MD0_DATA/Qmultimedia/backup-dir"
   # For grepping the available space
   BACKUP_SHARE_MOUNT_POINT="/share/MD0_DATA"

   SUBJ_PREFIX="[RSYNC BACKUP]"
   SUBJECT_FAIL="${SUBJ_PREFIX} Synchronization unsuccessful!"
   SUBJECT_SUCCESS="${SUBJ_PREFIX} Synchronization successful!"

   MAIL_TO="hansi@example.com"
   MAIL_FROM="hansi-machine@example.com"

   MSG_CONTENT_FILE="/tmp/email_body.txt"
   RSYNC_CONTENT_FILE="/tmp/rsyncContent.txt"

   # Sends a mail message
   # $1 = subject
   # $2 = to
   # $3 = from
   # $4 = msg
   send_mail()
   {
   local tmpfile="/tmp/sendmail.tmp"
   /bin/echo -e "Subject: $1\r" > "$tmpfile"
   /bin/echo -e "To: $2\r" >> "$tmpfile"
   /bin/echo -e "From: $3\r" >> "$tmpfile"
   /bin/echo -e "\r" >> "$tmpfile"
   if [ -f "$4" ]; then
      cat "$4" >> "$tmpfile"
      /bin/echo -e "\r\n" >> "$tmpfile"
   else
      /bin/echo -e "$4\r\n" >> "$tmpfile"
   fi
   /usr/sbin/sendmail -t < "$tmpfile"
   rm $tmpfile
   }

   /bin/echo -e "Backup started at: $(date)\n" > ${MSG_CONTENT_FILE}
   /bin/echo "Space available on backup disk (before backup)" >> ${MSG_CONTENT_FILE}
   /bin/echo "$(df -h | head -n1)" >> ${MSG_CONTENT_FILE}
   /bin/echo "$(df -h | grep ${BACKUP_SHARE_MOUNT_POINT})" >> ${MSG_CONTENT_FILE}
   /bin/echo -e "\n" >> ${MSG_CONTENT_FILE}

   /bin/echo -e "The following output was produced by the rsync command:\n" > ${RSYNC_CONTENT_FILE}
   /usr/bin/rsync -azhv ${BACKUP_SOURCE_USER}@${BACKUP_SOURCE_HOST}:${BACKUP_SOURCE_FOLDER} ${BACKUP_SINK_FOLDER} >> ${RSYNC_CONTENT_FILE}

   if [ $? -eq 0 ]
   then
   SUBJECT=${SUBJECT_SUCCESS}
   else
   SUBJECT=${SUBJECT_FAIL}
   fi

   /bin/echo -e "Backup ended at: $(date)\n" >> ${MSG_CONTENT_FILE}
   /bin/echo "Space available on backup disk (after backup)" >> ${MSG_CONTENT_FILE}
   /bin/echo "$(df -h | head -n1)" >> ${MSG_CONTENT_FILE}
   /bin/echo "$(df -h | grep ${BACKUP_SHARE_MOUNT_POINT})" >> ${MSG_CONTENT_FILE}
   /bin/echo -e "\n" >> ${MSG_CONTENT_FILE}

   /bin/cat ${RSYNC_CONTENT_FILE} >> ${MSG_CONTENT_FILE}

   send_mail "${SUBJECT}" "${MAIL_TO}" "${MAIL_FROM}" ${MSG_CONTENT_FILE}

Get the `Gist here <https://gist.github.com/authsec/42b3c5099e1cc45bc761c8bb6366ede8>`_!

Executing a Daily Backup
************************

Once the script is in it's place you need to configure a cron job so it will be executed let's say once a day at 5 AM. To do so simply type :code:`crontab -e` into your terminal as admin user and add a line like this at the end of the file.

.. code-block:: cron

    0 5 * * * /share/MD0_DATA/backupRemoteDirToQnap.sh

There you have it, nice and shiny backup.
