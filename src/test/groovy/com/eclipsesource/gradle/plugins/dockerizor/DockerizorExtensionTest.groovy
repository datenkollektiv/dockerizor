package com.eclipsesource.gradle.plugins.dockerizor
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import org.junit.Test

class DockerizorExtensionTest {

	@Test
	public void shouldSupportLatestVersionOfVirgoRapServer() {
		DockerizorExtension uut = new DockerizorExtension (
				virgoVersion:'latest',
				virgoFlavour:'VRS'
				)

		assertTrue(uut.archiveName.contains('virgo-nano-rap'))
		assertTrue(uut.archiveName.contains('latest'))
	}
}
