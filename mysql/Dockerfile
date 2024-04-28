# Dockerfile
FROM mysql
COPY init.sh /tmp/init.sh
COPY createHabitoDatabase.sql /docker-entrypoint-initdb.d/createHabitoDatabase.sql
RUN chmod +x /tmp/init.sh