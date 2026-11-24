#!/bin/sh
set -e
cd "$(dirname "$0")"
docker exec -i restart-mysql-1 mysql -urestart -prestart restart < cleanup-local.sql
