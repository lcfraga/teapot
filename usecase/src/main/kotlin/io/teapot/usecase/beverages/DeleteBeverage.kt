package io.teapot.usecase.beverages

import io.teapot.usecase.beverages.port.DeleteBeveragePort
import io.teapot.usecase.beverages.port.FindBeveragePort

sealed class DeleteBeverageResult {
    object Deleted : DeleteBeverageResult()
    object NotFound : DeleteBeverageResult()
}

class DeleteBeverage(
    private val findBeveragePort: FindBeveragePort,
    private val deleteBeveragePort: DeleteBeveragePort
) {

    fun deleteById(id: String): DeleteBeverageResult {
        if (!findBeveragePort.findById(id).isPresent) {
            return DeleteBeverageResult.NotFound
        }

        deleteBeveragePort.deleteById(id)

        return DeleteBeverageResult.Deleted
    }
}
