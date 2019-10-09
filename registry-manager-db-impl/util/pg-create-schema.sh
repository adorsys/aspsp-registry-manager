#!/usr/bin/env bash

# create aspsp_registry schema and give permissions to cms user. Needed for
# docker-compose so that we can start the DB with the schema already being
# present (rat)
set -e
echo "Create schema='aspsp_registry' for local postgres installation"
psql -U postgres -d aspsp_registry -c 'CREATE SCHEMA IF NOT EXISTS aspsp_registry AUTHORIZATION manager;'
