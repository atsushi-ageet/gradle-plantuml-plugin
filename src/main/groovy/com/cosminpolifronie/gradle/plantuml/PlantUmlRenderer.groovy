package com.cosminpolifronie.gradle.plantuml

import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

import javax.inject.Inject

public abstract class PlantUmlRenderer implements WorkAction<PlantUmlRenderer.PlantUmlRendererParameters> {
    public static interface PlantUmlRendererParameters extends WorkParameters {
        Property<PlantUmlPreparedRender> getPreparedRender(); 
    }

    @Override
    void execute() {
        // TODO: the following can be simplified when/if the following pull request gets accepted
        // https://github.com/plantuml/plantuml/pull/220
        // all below becomes
        /*
        preparedRender.output.withOutputStream { out ->
            new SourceStringReader(preparedRender.input.text).outputImage(out, new FileFormatOption(preparedRender.format, preparedRender.withMetadata))
        }
        */

        def preparedRender = getParameters().getPreparedRender().get()

        def fileFormatOption = new FileFormatOption(preparedRender.format)

        if (!preparedRender.withMetadata) {
            fileFormatOption.hideMetadata()
        }

        preparedRender.output.withOutputStream { out ->
            new SourceStringReader(preparedRender.input.text).outputImage(out, fileFormatOption)
        }
    }
}
