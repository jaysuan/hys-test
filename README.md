## Requirements
* JDK 11+
* Docker 1.13.0+

## Environment Variables Setup
* `CLIENT_ID` - Auth0 client ID

* `CLIENT_SECRET` - Auth0 client secret

* `RESOURCE_URI` - {resource-server-host:port}/api

* `ISSUER_URI` - Auth0 issuer URI

* `JWK_SET_URI` - {ISSUER_URI}/.well-known/jwk.json

## Testing
Run `mvn test` to start tests. No need to set up environment
variables when testing.


## Building
Run `mvn package` to build executable JAR files and Docker images
