package org.secwallet.core.security;


import org.secwallet.core.security.mapper.SecUserMapper;
import org.secwallet.core.model.SecUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SecUserMapper secUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecUser user = secUserMapper.findByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("this user does not exist");
        }
        //List of user permissions, according to the user's permission ID and the interface annotated with @PreAuthorize("hasAuthority('sys:menu:view')") to determine whether the interface can be called
        //Set<String> permissions =null; //sysUserService.findPermissions(user.getName());
        //List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        return new SecUser(
                user.getId(),user.getNick_name(), user.getMobile(), user.getLogin_password());
    }
}