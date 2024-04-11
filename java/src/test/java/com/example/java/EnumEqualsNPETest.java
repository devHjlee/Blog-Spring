package com.example.java;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EnumEqualsNPETest {
    @Test
    void checkEnumNpe() {
        // given
        MemberDto memberDto = new MemberDto("memberA", null);

        // when then
        // joinYn NPE 발생
        assertThrows(NullPointerException.class, () -> {memberDto.joinYn.equals(TypeYn.N);});
        // NPE 피할수 있음
        assertThat(TypeYn.N.equals(memberDto.joinYn)).isEqualTo(false);
        assertThat(memberDto.joinYn == (TypeYn.N)).isEqualTo(false);

        // Equals 사용시 NPE는 피할수 있지만, 맞지 않는 타입에 대한 오류발견이 늦어 질 수 있음
        assertThat(YnTYpe.Y.equals(memberDto.joinYn)).isEqualTo(false);
        // 타입오류
        //memberDto.joinYn == TypeYn.Y;
    }
}

enum TypeYn {
    Y,N
}

enum YnTYpe {
    Y,N
}

class MemberDto {
    TypeYn joinYn;
    String name;

    public MemberDto(String name,TypeYn joinYn) {
        this.name = name;
        this.joinYn = joinYn;
    }
}
