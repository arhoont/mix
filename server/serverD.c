#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <syslog.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>

int port;
char *ver;
char *pswd;
int sink;
int readConfig(){
    FILE *file; 
    char *fname = "config";
    file = fopen(fname,"r");
    char str[5];
    fgets(str,sizeof(str),file);
    port=atoi(str);
    
    fgets(str,sizeof(str),file);
    sink=atoi(str);
    fclose(file);

}

int magic(char *buf){
    char *tokens[10];
    int i=0;
    char *token, *last;
//    printf ("Accept some msg from client\n");
    i=0;
    tokens[i] = strtok_r(buf, " ,", &last);
    while (tokens[i] != NULL) {
        i++;
        tokens[i] = strtok_r(NULL, " ", &last);
    }
    i--;
    if (strcmp(tokens[0],ver)){
//        printf("not ver\n");
        return 1;
    }
    if (strcmp(tokens[1],pswd)){
//        printf("not pswd\n");
        return 2;
    }
    if (strcmp(tokens[4],"end")){
//        printf("not end\n");
        return 3;
    }
	// volume
    if (!(strcmp(tokens[2],"volume"))){
        char str[100];
        sprintf(str,"pactl set-sink-volume '%d' %s",sink,tokens[3]);
        system(str);
//        printf("%s === \n",str);
    }
	// play
    if (!(strcmp(tokens[2],"play"))){
        system("rhythmbox-client --play");
    }
	// pause
    if (!(strcmp(tokens[2],"pause"))){
        system("rhythmbox-client --pause");
    }
	// next
    if (!(strcmp(tokens[2],"next"))){
        system("rhythmbox-client --next");
    }
	// prev
    if (!(strcmp(tokens[2],"prev"))){
        system("rhythmbox-client --previous");
    }
    if (!(strcmp(tokens[2],"enter"))){
        system("xdotool key KP_Enter");
    }
    if (!(strcmp(tokens[2],"left"))){
        system("xdotool key KP_Left");
    }
    if (!(strcmp(tokens[2],"right"))){
        system("xdotool key KP_Right");
    }
}
void becomeADaemon(){
    pid_t pid, sid;
    pid = fork();
    if (pid < 0) {
		exit(EXIT_FAILURE);
    }
    if (pid > 0) {
		exit(EXIT_SUCCESS);
    }
    umask(0);
    /* Open any logs here */        
	openlog("Mixer log", LOG_PID | LOG_CONS, LOG_DAEMON);
    /* Create a new SID for the child process */
    sid = setsid();
    if (sid < 0) {
		syslog(LOG_INFO, "can't set sid");
	    exit(EXIT_FAILURE);
    }
    /* Change the current working directory */
    if ((chdir("/")) < 0) {
		syslog(LOG_INFO, "can't change dir");
        exit(EXIT_FAILURE);
    }
        
    /* Close out the standard file descriptors */
    close(STDIN_FILENO);
    close(STDOUT_FILENO);
    close(STDERR_FILENO);
	syslog(LOG_INFO, "Daemon started");
	closelog();
}
int main(int argc, char **argv){
    openlog("Some test log", LOG_PID | LOG_CONS, LOG_DAEMON);
	if(argc <= 1) {
		syslog(LOG_INFO, "Can't start daemon, no password");
		return 1;
    }
    readConfig();
    pswd=argv[1];
    ver="v1";
	becomeADaemon();
    int sock, listener;
    struct sockaddr_in addr;
    char buf[1024];
    int bytes_read;
    listener = socket(AF_INET, SOCK_STREAM, 0);
    if(listener < 0)
    {
        perror("socket");
        exit(1);
    }
    
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    if(bind(listener, (struct sockaddr *)&addr, sizeof(addr)) < 0)
    {
        perror("bind");
        exit(2);
    }

    listen(listener, 1);
    syslog(LOG_INFO, "Daemon started on");
    closelog();

    while(1)
    {
        sock = accept(listener, NULL, NULL);
        if(sock < 0)
        {
            perror("accept");
            exit(3);
        }

        while(1)
        {
            bytes_read = recv(sock, buf, 1024, 0);
            if(bytes_read <= 0) break;
            send(sock, "ok\n", bytes_read, 0);
        }
        magic(buf);        
        close(sock);
    }
    
    return 0;
}
