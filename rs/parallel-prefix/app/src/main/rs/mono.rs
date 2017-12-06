#pragma version(1)
#pragma rs java_package_name(edu.cs.p750.parPrefixRS)

const int *sequence;
int *output;

void __attribute__((kernel)) parPrefix(int in, uint32_t x){
    int i;
    int sum = 0;
    for(i=0;i<=in;i++){
        sum = sum + sequence[i];
    }
    output[in] = sum;
}