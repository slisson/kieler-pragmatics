/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.grana.text.validation

import de.cau.cs.kieler.grana.AnalysisService
import de.cau.cs.kieler.grana.text.grana.Analysis
import de.cau.cs.kieler.grana.text.grana.GranaPackage
import de.cau.cs.kieler.grana.text.grana.OutputType
import de.cau.cs.kieler.grana.text.grana.RangeJob
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class GranaValidator extends AbstractGranaValidator {
	
    @Check 
    def checkAnalysisExists(Analysis analysis) {
        if (AnalysisService.instance.getAnalysis(analysis.name) === null) {
            error("Unknown analysis: " + analysis, GranaPackage.Literals.ANALYSIS__NAME)
        }
    }

    @Check
    def checkRangeAnalysisComponentExists(RangeJob job) {
        val analysis = AnalysisService.getInstance.getAnalysis(job.rangeAnalysis.name)
        if (analysis !== null) {
            if (job.rangeAnalysisComponent < 0) {
                error("Component must be >= 0.", GranaPackage.Literals.RANGE_JOB__RANGE_ANALYSIS_COMPONENT)
            }
            if ((analysis.components === null || analysis.components.isEmpty) && job.rangeAnalysisComponent != 0) {
                error("Selected range analysis has only a single component, 'component' must be 0.",
                    GranaPackage.Literals.RANGE_JOB__RANGE_ANALYSIS_COMPONENT)
            }
            if (!analysis.components.isEmpty && analysis.components.size <= job.rangeAnalysisComponent) {
                error("Selected range analysis has only " + analysis.components.size + " components.",
                    GranaPackage.Literals.RANGE_JOB__RANGE_ANALYSIS_COMPONENT)
            }
        }
    }
    
    @Check
    def checkRangeOptionUnique(RangeJob job) {
        val layoutOptions = job.layoutOptions.map[it.properties.map[it.key]].flatten.toSet
        val rangeOption = job.rangeOption
         
        if (layoutOptions.contains(rangeOption)) {
            error("Range option cannot be part of the original layout options.",
                GranaPackage.Literals.RANGE_JOB__RANGE_OPTION)
        }
    }
    
    @Check
    def checkCsvSerializeRequiresSingleRangeAnalysis(RangeJob job) {
        if (job.outputType == OutputType.CSV) {

            if (!job.rangeAnalyses.isEmpty) {
                error("CSV serialization only supports a single 'rangeanalysis', not multiple 'rangeanalyses'.",
                    GranaPackage.Literals.RANGE_JOB__RANGE_ANALYSES)
            }
            
        }
    }

	
}
