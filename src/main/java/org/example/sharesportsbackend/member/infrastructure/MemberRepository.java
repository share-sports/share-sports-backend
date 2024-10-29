package org.example.sharesportsbackend.member.infrastructure;

import java.util.Optional;

import org.example.sharesportsbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Member> findByMemberUuid(String memberUuid);
    @Query("SELECT m.email FROM Member m WHERE m.memberUuid = :memberUuid")
    String findEmailByMemberUuid(@Param("memberUuid") String memberUuid);
}
