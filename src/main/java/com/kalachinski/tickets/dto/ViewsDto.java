package com.kalachinski.tickets.dto;

public final class ViewsDto {

    public interface Exist{}

    public interface New extends Exist{}

    public interface Id{}

    public interface IdName extends Id{}

    public interface FullViews extends IdName{}

}
