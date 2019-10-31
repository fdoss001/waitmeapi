del spdump_app.sql
echo. & echo. >> spdump_app.sql
for %%f in (.\app\*) do (@type %%f >> spdump_app.sql & echo. >> spdump_app.sql & echo. >> spdump_app.sql)