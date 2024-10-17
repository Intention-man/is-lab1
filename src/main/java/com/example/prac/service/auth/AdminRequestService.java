package com.example.prac.service.auth;


import com.example.prac.model.authEntity.AdminRequest;
import com.example.prac.model.authEntity.Role;
import com.example.prac.model.authEntity.User;
import com.example.prac.repository.auth.AdminRequestRepository;
import com.example.prac.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;

    public boolean createAdminRequest(AdminRequest adminRequest) {
        if (getAdminRequestById(adminRequest.getId()).isEmpty()) {
            adminRequestRepository.save(adminRequest);
            return true;
        }
        return false;
    }

    public Optional<AdminRequest> findByRequester(User requester) {
        return adminRequestRepository.findByRequester(requester);
    }

    public Optional<AdminRequest> getAdminRequestById(Long id) {
        return adminRequestRepository.findById(id);
    }

    public void updateAdminRequest(AdminRequest adminRequest) {
        adminRequestRepository.save(adminRequest);
    }

    public List<User> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }

    @Transactional(readOnly = true) // TODO может стоит убрать эту аннотацию
    public List<AdminRequest> getAllAdminRequests() {
        return (List<AdminRequest>) adminRequestRepository.findAll();
    }

    public boolean approveRequest(Long requestId, User currentUser) throws Exception {
        AdminRequest adminRequest = adminRequestRepository.findById(requestId);

        if (adminRequest == null) {
            return false;
        }
        if (!adminRequest.getApprovedBy().stream().map(User::getUsername).toList().contains(currentUser.getUsername())) {
            adminRequest.getApprovedBy().add(currentUser);
            List<User> allAdmins = userRepository.findAllAdmins();

            if (adminRequest.getApprovedBy().stream().map(User::getUsername).toList().containsAll(allAdmins.stream().map(User::getUsername).toList())) {
                adminRequest.setApprovedByAll(true);
                User userToApprove = adminRequest.getRequester();
                promoteUserToAdmin(userToApprove);

                adminWebSocketHandler.sendNotificationToUser(userToApprove.getUsername(), "Your admin request has been approved!");
            }
            this.updateAdminRequest(adminRequest);
            return true;
        }

        return false;
    }

    public void promoteUserToAdmin(User user) {
        user.setRole(Role.ADMIN);
        userRepository.update(user);
    }
}


//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final AdminRequestRepository adminRequestRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserService(UserRepository userRepository, RoleRepository roleRepository,
//                       AdminRequestRepository adminRequestRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository =
//
//
//                userRepository;
//        this.roleRepository = roleRepository;
//        this.adminRequestRepository = adminRequestRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User registerNewUser(UserDTO userDTO) {
//        User user = new User();
//        user.setUsername(userDTO.getUsername());
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        user.setEnabled(true);
//
//        if (adminExists()) {
//            throw new IllegalStateException("Admin approval required");
//        }
//
//        Role userRole = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("User Role not set"));
//        user.setRoles(Set.of(userRole));
//        return userRepository.save(user);
//    }
//
//    private boolean adminExists() {
//        return userRepository.findAll().stream().anyMatch(user -> user.getRoles().stream()
//                .anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
//    }
//
//    public AdminRequest requestAdminRole(String username) {
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent() && !isAdmin(user.get())) {
//            AdminRequest request = new AdminRequest();
//            request.setUsername(username);
//            request.setStatus(RequestStatus.PENDING);
//            return adminRequestRepository.save(request);
//        }
//        throw new IllegalArgumentException("User not found or is already admin");
//    }
//
//    private boolean isAdmin(User user) {
//        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
//    }
//
//    public void approveAdminRequest(Long requestId, User approver) {
//        AdminRequest request = adminRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
//        if (isAdmin(approver)) {
//            User user = userRepository.findByUsername(request.getUsername())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
//                    .orElseThrow(() -> new RuntimeException("Admin Role not set"));
//            user.getRoles().add(adminRole);
//            userRepository.save(user);
//            request.setStatus(RequestStatus.APPROVED);
//            request.setApprover(approver);
//            adminRequestRepository.save(request);
//        } else {
//            throw new AccessDeniedException("Only admins can approve requests");
//        }
//    }
//}
