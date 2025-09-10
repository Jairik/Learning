#include <mpi.h>
#include <stdio.h>

int main(int argc, char** argv){
    MPI_Init(NULL, NULL);  // Initializing the MPI environment
    int world_size;  // Number of processes
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    
    int world_rank;  // Rank of the process
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    // Get the name of the processor
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    int name_len;
    MPI_Get_processor_name(processor_name, &name_len);

    printf("I am %d of %d on %s\n", world_rank, world_size, processor_name);

    MPI_Finalize();  // Finalizing the MPI environment
    return 0;
}