import configuration.DependencyVersions

plugins {
    java
}

dependencies {
    implementation("org.hibernate.validator:hibernate-validator:${DependencyVersions.HIBERNATE_VALIDATOR}")
    implementation("org.glassfish:javax.el:${DependencyVersions.EL_API}")
}
