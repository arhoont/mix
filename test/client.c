#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>

char message[] = "v1 123aaa volume 50% end asd!";
char buf[sizeof(message)];

int main(int argc, char *argv[])
{
    char str[100];
    sprintf(str,"v1 123aaa volume %s end",argv[1]);

    int sock;
    struct sockaddr_in addr;

    sock = socket(AF_INET, SOCK_STREAM, 0);
    if(sock < 0)
    {
        perror("socket");
        exit(1);
    }

    addr.sin_family = AF_INET;
    addr.sin_port = htons(3426); // или любой другой порт...
    addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);
    if(connect(sock, (struct sockaddr *)&addr, sizeof(addr)) < 0)
    {
        perror("connect");
        exit(2);
    }

    send(sock, str, sizeof(str), 0);
    recv(sock, buf, sizeof(message), 0);
    
    printf(buf);
    close(sock);

    return 0;
}
