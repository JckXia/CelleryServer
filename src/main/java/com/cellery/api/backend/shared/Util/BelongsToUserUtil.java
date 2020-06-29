package com.cellery.api.backend.shared.Util;

import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class BelongsToUserUtil {
    @Autowired
    private UsersRepository usersRepository;

    public boolean routineBelongsToUser(String email, String routineId) {
        UserEntity user = usersRepository.findByEmail(email);
        return user.getRoutines().stream().anyMatch(routine -> routine.getRoutineId().equals(routineId));
    }

    public boolean productBelongsToUser(String email, String productId) {
        UserEntity user = usersRepository.findByEmail(email);
        return user.getUserProducts().stream().anyMatch(product -> product.getProductId().equals(productId));
    }

    public void productsBelongToUser(String email, List<String> productIds) throws FileNotFoundException {
        // check if products belong to user
        for (String productId: productIds) {
            if (!productBelongsToUser(email, productId)) {
                throw new FileNotFoundException("Product does not exist");
            }
        }
    }
}
