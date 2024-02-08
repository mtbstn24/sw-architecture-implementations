# Client-server architecture using threads

<b>Compile</b><br>
`javac -d build/classes *.java`

<b>Run</b><br>
<b>Server</b> --> `java -cp build/classes PubSub` or `java -cp build/classes PubSub <port> <backlog> <serverip>` <br>
eg: java -cp build/classes PubSub 8080 0 localhost

<b>Client</b> --> `java -cp build/classes Client <serverip> <port> <username>` <br>
eg: java -cp build/classes Client localhost 8080 John
