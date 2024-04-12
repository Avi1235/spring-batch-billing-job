docker exec postgres psql -f /mnt/exercises/src/sql/schema-drop-postgresql.sql -U postgres
docker exec postgres psql -f /mnt/exercises/src/sql/schema-postgresql.sql -U postgres