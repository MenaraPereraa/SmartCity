package lk.menara.smartcity360.auth;

import lk.menara.smartcity360.entity.AppUser;
import lk.menara.smartcity360.util.JPAUtil;
import lk.menara.smartcity360.util.PasswordUtil;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class AuthService {

    public AppUser authenticate(String email, String rawPassword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<AppUser> query = em.createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email AND u.status = 'ACTIVE'", AppUser.class);
            query.setParameter("email", email);

            AppUser user = query.getSingleResult();

            if (user != null && user.getPasswordHash().equals(rawPassword)) {
                return user;
            }
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
        return null;
    }
}