package ru.niceaska.gameproject.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Скоуп для инжекта презентеров
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ScreenScope {

}