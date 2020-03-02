package com.schaaf.foxweather.dagger

import javax.inject.Scope
import javax.inject.Qualifier

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CelsiusRadioButton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FahrenheitRadioButton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class mmHgRadioButton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class hPaRadioButton