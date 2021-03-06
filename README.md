CellBase
========

High-Performance NoSQL database and RESTful web services to access to most relevant biological data.


This repository comes from a major rewrite and refactoring done from previous versions, old code can be found at: https://github.com/opencb-cloud



CONTACT
------- 
  You can contact any of the following developers:

    * Javier Lopez (fjavier@bioinfomgp.org)
    * Ignacio Medina (imedina@ebi.ac.uk)


DOWNLOAD
--------

  CellBase is open to the community and released in GitHub, so you can download by invoking the following commands:

    $ git clone https://github.com/opencb/cellbase.git
    Cloning into 'cellbase'...
    remote: Counting objects: 6653, done.
    remote: Total 6653 (delta 0), reused 0 (delta 0)
    Receiving objects: 100% (6653/6653), 4.80 MiB | 1.20 MiB/s, done.
    Resolving deltas: 100% (2651/2651), done.

BUILDING 
--------

  The build process is managed by Maven scripts. CellBase building requires Java 7 and a set of dependencies which are already uploaded to the Maven repository. CellBase also depends on the ENSEMBL API version 76, which may be installed following this tutorial:

http://www.ensembl.org/info/docs/api/api_installation.html
  
  Build CellBase:
  
    $ mvn clean install -DskipTests


DOCUMENTATION
-------------

  You can find more info about CellBase project at: https://github.com/opencb/cellbase/wiki

  
