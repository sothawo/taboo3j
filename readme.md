# taboo3j

next implementation of my tagged bookmark service, this time with:

* Spring-Boot
* Spring MVC with Thymeleaf
* spring-data-elasticsearch as backend

## users, passwords and roles

the configuration needs to reference a file with user data when _security.basic.enabled_ is configured to _true_. The
 configuration entry is _taboo3.users_
 
This file has lines with the following content:

_username:hashedPassword:role1,...,roleN_

The hashedPassword can be created with the _main_ method of the Taboo3UserService class.

## bookmarklet

The following bookmarklet can be used to send the url of the current page to the application (adapt the host and port):

    javascript: (function() {var newLocation='http://localhost:8080/bookmark/add?url=' + encodeURIComponent(document.location.href); open(newLocation).focus();})();

## bulk upload

json data (exported from this program or taboo2) can be loaded with:

    curl -XPOST -H "Content-Type: application/json" -d @filename -u user:password http://localhost:8080/bookmark/upload
