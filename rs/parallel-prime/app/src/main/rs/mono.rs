#pragma version(1)
#pragma rs java_package_name(edu.cs.p750.prime)


int __attribute__((kernel)) isPrime(int n, uint32_t x){
    int flag = 0;
    for(int i=2;i<n;i++){
        if(n%i == 0){
            flag = 1;
            break;
        }
    }
    return flag;
}