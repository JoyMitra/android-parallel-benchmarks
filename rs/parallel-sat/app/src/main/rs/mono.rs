#pragma version(1)
#pragma rs java_package_name(edu.cs.p750.satisfy)

const int *input;
const int* var_num;

int __attribute__((kernel)) satisfy(int rowId, uint32_t x){
    int n = var_num[0];
    int z = 0;
    bool issat = true;
    for(int i=rowId;i<rowId+n;i++){
        bool b;
        if(input[i] == 0) b = false;
        else if(input[i] == 1) b = true;
        issat = issat && b;
        //if(i%2==0)
          //  issat = issat && b;
        //else issat = issat || b;
    }
    if(issat) return 1;
    else return 0;
}