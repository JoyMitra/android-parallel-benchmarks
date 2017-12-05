#pragma version(1)
#pragma rs java_package_name(edu.cs.p750.findMax)

const int *in_arr;
int *out_arr;

void __attribute__((kernel)) max2(int in, uint32_t x){
    if(x % 2 == 0){
        if(in_arr[x] > in_arr[x+1]){
            out_arr[x/2] = in_arr[x];
        }
        else{
            out_arr[x/2] = in_arr[x+1];
        }
    }
}