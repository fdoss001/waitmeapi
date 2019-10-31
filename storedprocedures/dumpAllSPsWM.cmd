del spdump_wm.sql
echo. & echo. >> spdump_wm.sql
for %%f in (.\waitme\*) do (@type %%f >> spdump_wm.sql & echo. >> spdump_wm.sql & echo. >> spdump_wm.sql)