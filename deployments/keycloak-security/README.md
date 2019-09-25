# Local Minio Deployment

## Description
This is a local sample deployment that protects an ion-ingest, an ion-store, and an ion-search service behind a
a gateway service. The gateway service uses a keycloak instance for an oauth IDP, and minio as
Amazon S3 compatible storage for the ingest service.

## Prerequisites
* Java 11
* Docker daemon

## How to Run
### Deploying the images
From this directory, run the following script:
```
./deploy.sh
```
The script should provide appropriate error messages to resolve any issues that may occur while deploying the images.

The docker stack can be completely shutdown by running:
```
./deploy.sh -c
```

For additional options, run the deploy script with the `--help` option.

## Accessing the services
### Hosts file additions
Add the following lines to your hosts file before deploying the images:
```
127.0.0.1	ion
127.0.0.1	keycloak
```
If accessing the machine remotely rather than locally, update the IP address as needed to point to the machine
hosting these services.

### Routes
The gateway service runs on port 80. All internal services are accessed by going through the gateway with the
appropriate context path. These context paths and routes can be found and modified in `configs/gateway_config.yml`
under `spring->cloud->gateway->routes`.

For example, with the hosts configuration above and the default routes set, navigating to `http://ion:80/minio` will
redirect the user to login with keycloak, and then subsequently redirect them back to the gateway, forwarding the
request to the internal minio service. All requests sent to `/mino` will continue be forwarded to the minio server.

### Authorization methods
#### Browser redirect
If the user is accessing the gateway with a browser, they will automatically be redirected to keycloak to sign in.
Upon successful authentication with Keycloak, they will be assigned a sessionid and all requests will be forwarded
through the gateway to internal services along with the user's attributes in the form of an access token.

#### System to system & non-browser requests
If the user should not be redirected to Keycloak, such as for system-to-system messages or testing with Postman, then
an access token can be requested from keycloak directly and added to the Authorization header as a Bearer token.
For example:
```
Authorization: Bearer <ACCESS_TOKEN_STRING>
```
For convenience, the `access_token.sh` script in this directory can be run to automatically retrieve an access token
for the `admin` user from keycloak that can be directly used in an Authorization header. This script will only work
with the example keycloak configuration used by default in this deployment.

## Services

### Minio
The minio web client can be accessed through the gateway at the `/minio` context by default with the login
information:
```
Access Key: MINIOEXAMPLEACCESSKEY
Secret key: MINIOEXAMPLESECRETKEY
```
These keys can also be found in `secrets/minio_access.notsec` and `secrets/minio_secret.notsec`

### Keycloak
A keycloak instance will be deployed on port 9000. As this is a login portal, it is not necessary to access this
service through the gateway. This default instance comes with example users:
```
admin:admin
```
