/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.grana.text


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class GranaStandaloneSetup extends GranaStandaloneSetupGenerated {

	def static void doSetup() {
		new GranaStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
