#pragma version(1)
#pragma rs java_package_name(edu.cs.p750.matrixmult)

const float *matA;
const float *matB;
float *outMatrix;
int *nSize;
int *kSize;

void root(const int *v_in1, int *v_out) {
    int row_idx = *v_in1;
    int n = *nSize;
	int k = *kSize;

	for(int i=0; i<n; i++){
		for(int j=0; j<k; j++){
			outMatrix[row_idx*n+i] += matA[row_idx*k+j] * matB[i*k+j];
		}
	}

}