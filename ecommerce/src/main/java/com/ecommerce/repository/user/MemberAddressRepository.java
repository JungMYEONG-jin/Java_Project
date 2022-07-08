package com.ecommerce.repository.user;

import com.ecommerce.entity.user.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

        List<MemberAddress> findAllByUserIdOrderByMainDesc(long userId);
        Optional<MemberAddress> findByIdAndUserId(long addressId, long userId);
        void deleteByIdAndUserId(long addressId, long userId);
        Optional<MemberAddress> findByUserIdAndMain(Long userId, boolean main);


}
