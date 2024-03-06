package com.example.demo11.domain;

import java.util.Objects;

public class Invite extends Entity<Long>{
    private Long fromInvite;
    private Long toInvite;
    private String status;

    public Invite(Long fromInvite, Long toInvite, String status) {
        this.fromInvite = fromInvite;
        this.toInvite = toInvite;
        this.status = status;
    }

    public Long getFromInvite() {
        return fromInvite;
    }

    public void setFromInvite(Long fromInvite) {
        this.fromInvite = fromInvite;
    }

    public Long getToInvite() {
        return toInvite;
    }

    public void setToInvite(Long toInvite) {
        this.toInvite = toInvite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "fromInvite=" + fromInvite +
                ", toInvite=" + toInvite +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invite invite)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(fromInvite, invite.fromInvite) && Objects.equals(toInvite, invite.toInvite) && status == invite.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromInvite, toInvite, status);
    }
}
