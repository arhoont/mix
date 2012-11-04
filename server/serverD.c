#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
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

    printf("%d\n",port);
    printf("%d\n",sink);
}

int magic(char *buf){
    char *tokens[10];
    int i=0;
    char *token, *last;
    printf ("Accept some msg from client\n");
    i=0;
    tokens[i] = strtok_r(buf, " ,", &last);
    while (tokens[i] != NULL) {
        i++;
        tokens[i] = strtok_r(NULL, " ", &last);
    }
    i--;
    if (strcmp(tokens[0],ver)){
        printf("not ver\n");
        return 1;
    }
    if (strcmp(tokens[1],pswd)){
        printf("not pswd");
        return 2;
    }
    if (strcmp(tokens[4],"end")){
        return 3;
    }
    if (!(strcmp(tokens[2],"volume"))){
        char str[100];
//        printf("%d ttt\n",sink);
//        printf("%s ttt\n",tokens[3]);
//        printf("'%d' --- %s",sink,tokens[3]);
        
        sprintf(str,"pactl set-sink-volume '%d' %s",sink,tokens[3]);
        system(str);
        printf("%s === \n",str);
    }
}

int main()
{
    readConfig();
    pswd="123aaa";
    ver="v1";
    printf("start\n");
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
