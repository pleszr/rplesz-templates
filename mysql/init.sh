#!/bin/bash

echo "Creating habito_admin user"
cat <<EOF > /docker-entrypoint-initdb.d/init.sql
CREATE USER 'habito_admin'@'%' IDENTIFIED BY '${HABITO_ADMIN_PASSWORD}';
GRANT ALL PRIVILEGES ON *.* TO 'habito_admin'@'%';
FLUSH PRIVILEGES;
EOF
echo "If no error above, habito_admin user successfully created"