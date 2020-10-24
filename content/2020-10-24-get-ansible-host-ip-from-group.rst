Get ansible_host from host group
################################

:date: 2020-10-24T18:56:00+01:00
:tags: Ansible
:category: Infrastructure
:summary: How to get the IP address from host group.

Assume you are working with this inventory (:code:`hosts`) file::

   [bootstrap]
   opihi.example.com ansible_host=192.168.30.254 ansible_user=you ansible_password='{{ vault_opihi_you_user_password }}' ansible_python_interpreter=/usr/bin/python3

then you can get the IP address with the expression :code:`{{ hostvars[groups['bootstrap'][0]].ansible_host }}`.

Here is an example playbook::

  ---
  - hosts: localhost

    tasks:
        - debug: msg={{ hostvars[groups['bootstrap'][0]].ansible_host }}

Which will output::

  $#> ansible-playbook -i inventories/prod/ debug.yml

      PLAY [localhost] ********************************************************************************************************************************

      TASK [Gathering Facts] **************************************************************************************************************************
      ok: [localhost]

      TASK [debug] ************************************************************************************************************************************
      ok: [localhost] => {}

      MSG:

      192.168.30.254

      PLAY RECAP **************************************************************************************************************************************
      localhost                  : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0

Enjoy.