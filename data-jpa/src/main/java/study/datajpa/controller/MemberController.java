package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id)
    {
        Optional<Member> member = memberRepository.findById(id);
        return member.get().getUsername();
    }

    @GetMapping("/members2/{id}") // domain converter
    public String findMember2(@PathVariable("id") Member member)
    {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size=5) Pageable pageable)
    {
        return memberRepository.findAll(pageable);
    }


    // 절대 entity 자체를 반환하면 안됨. 위험!
    @GetMapping("/membersDto")
    public Page<MemberDto> listDto(@PageableDefault(size=5) Pageable pageable)
    {
        return memberRepository.findAll(pageable).map(m -> new MemberDto(m.getId(), m.getUsername(), "teamA"));
    }


    @PostConstruct
    public void init()
    {
        for(int i=0;i<100;i++)
        {
            memberRepository.save(new Member("user"+i, i));
        }
    }

}
