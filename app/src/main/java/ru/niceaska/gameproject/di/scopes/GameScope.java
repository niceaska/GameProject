package ru.niceaska.gameproject.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Скоуп интерактооров
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface GameScope {

}
