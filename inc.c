#include <stdio.h>

int x = 0;
int y = 0;

void f() {
    y ++;
}

int main(void) {
    int i = 0;
    for (i=0; i<300; i++) {
        x ++;
        int j = 0;
        for (j=0; j<5; j++) {
            y++;
        }
    }

    return 0;
}
