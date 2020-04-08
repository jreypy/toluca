package py.com.roshka.truco.server.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.server.service.TrucoUserService;

@Component
public class TrucoUserServiceImpl implements TrucoUserService {
    @Override
    public TrucoUser getTrucoUser(String authKey) {
        String username = authKey.split("-")[0];
        TrucoUser trucoUser = new TrucoUser();
        trucoUser.setId(username);
        trucoUser.setUsername(username);
        return trucoUser;
    }

    @Override
    public TrucoUser getTrucoUser() {
        TrucoPrincipal trucoPrincipal = (TrucoPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getTrucoUser(trucoPrincipal.getAuthKey());
    }
}
