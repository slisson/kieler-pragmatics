/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2013 by
 * + Kiel University
 *     + Department of Computer Science
 *         + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.kwebs.server.web

import de.cau.cs.kieler.kwebs.server.layout.ServerLayoutMetaDataService

/**
 * Presents a HTML page with input elements for a textual graph element and a config area.
 * Upon pressing a 'layout' button, the layouted graph is presented as an svg.
 * 
 * @author uru
 */
class LiveProvider extends AbstractProvider {

    // default formats
	private val String DEFAULT_INPUT_FORMAT = "org.json"
	private val String DEFAULT_OUTPUT_FORMAT = "org.w3.svg"

	/**
	 * 
	 */
	override getHeaders(ResourceProcessingExchange processingExchange) {
		'''
		««««<link href="styles/bootstrap-3.0.2.min.css" rel="stylesheet">
		<link href="styles/prettify.css" type="text/css" rel="stylesheet" />
		
		««««<script	src="scripts/jquery-1.10.2.min.js"></script>
		««««<script src="scripts/bootstrap-3.0.2.min.js"></script>
		<script src="scripts/jquery.event.drag.js"></script>
		<script src="scripts/jquery.mousewheel.js"></script>
		<script src="scripts/jquery.svg.js"></script>
		<script src="scripts/jquery.svg.extras.js"></script>
		<script src="scripts/prettify.js"></script>
		
		<style>
			body, html {
			  height: 100%;
				««««background-color: #5781BB;
			}
			
			.alert-error {
				font-size: 13px;
			}
		
			[class*="span"] {
				margin-left: 0px;	
			}
			
		  	#srcArea, #configArea {
				width: 100%;
				height: 300px;	
				font-size: 10px;
			}
			
			#resGraph {
				width: 100%;
				height: 100%;
			}
			
			.row {
			    margin-bottom: 5px;  
			}
			
			svg {
				width: 100%;
				height: 100%;
			}

			textarea {
				resize: none;
			}
			
			.pre-scrollable {
				max-height: 500px;
			}
			
			pre.prettyprint {
				border: 1px solid rgba(0, 0, 0, 0.15);
			}
		</style>
		'''
	}
	
	/**
	 * We specify our own container here to allow the svg more space
	 */
	override requireContainer() {
	  return false
	}
	
	/**
	 * 
	 */
	override getBody(ResourceProcessingExchange processingExchange) {
		val formats = ServerLayoutMetaDataService::getInstance.serviceDataModel.supportedFormats
		
		'''
			<div class="container">	
				<div class="row">
					<div id="srcGraph" class="col-md-8">
						<h4>Input Graph <small><a href="Providedlayout.html#formats">See Formats</a></small></h4>
			            <textarea id="srcArea">
			            </textarea>
					</div>
					<div id="config" class="col-md-4">
						<h4>Config <small><a href="Providedlayout.html#algorithms">See Layout Options</a></small></h4>
			            <textarea id="configArea">
			            </textarea>
					</div>
				</div>
				<div class="row">
				    <div class="col-md-12">
					<div class="col-md-3 input-group">
						<span class="input-group-addon">Input Format</span>
						<select id="inputFormat" class="form-control">
							«formats.sortBy[it.name].map(f |
								'''<option «if(f.id == DEFAULT_INPUT_FORMAT) '''selected="selected"'''» value="«f.id»">«f.name»</option>'''
							).join»
						</select>
					</div>
					<div class=" col-md-3 input-group">
						<span class="input-group-addon">Output Format</span>
						<select id="outputFormat" class="form-control">
							«formats.sortBy[it.name].map(f |
								'''<option «if(f.id == DEFAULT_OUTPUT_FORMAT) '''selected="selected"'''» value="«f.id»">«f.name»</option>'''
							).join»
						</select>
					</div>
					<button id="layout" class="btn pull-right" style="margin-right: 10px">Layout</button><span id="working"></span>
					</div>
				</div>
			
				<div class="row">
				    <div class="col-md-12">
					   <div id="errorDiv" class="alert alert-error" style="display: none;"></div>
					</div>
				</div>
			</div>	
			 
			 <!-- the svg gets the remaining size, i.e full display area -->
			 <div id="resGraph"></div>
			 
			<script>
			$(function() {
			  
			  // add tabbing behavior to the textareas
			  $('textarea').keydown(function(e) {
			  
			   if (e.keyCode === 9) { // tab
			     var start = this.selectionStart;
			     var end = this.selectionEnd;
			     
			     // replace textarea content with text before + tabs + text after
			     $(this).val($(this).val().substring(0, start)
			       + "    " // 4 spaces
			       + $(this).val().substring(end));
			       
			     // move cursor 
			     this.selectionStart = this.selectionEnd = start + 4;
			     
			     // prevent the default tab behavior
			     e.preventDefault();
			   }
			  });

				// init the svg with zoom and pan
				$('#resGraph').svg();
				$('#resGraph').addScalability();
				$('#resGraph').addDraggable();
				
				var svg = $('#resGraph');
				
				$('#layout').click(function() {
					var graph = $("#srcArea").val();
					var config = '{' + $('#configArea').val() + '}';
					var iFormat = $('#inputFormat > option:selected').val();
					var oFormat = $('#outputFormat > option:selected').val();
					
					$.ajax({
						type: 'POST',
						url: '/layout',
						contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
						data: {
							graph: graph,
							config: config,
							iFormat: iFormat,
							oFormat: oFormat
						}, 
						success: function(svggraph) {
						    
						    // answer is application/x-www-form-urlencoded
							svggraph = decodeURIComponent(svggraph.replace(/\+/g, '%20'));
							
							// if not svg, surround with pretty print
							if (oFormat != "org.w3.svg") {
							  // replace xml markup for displaying
			                  svggraph = svggraph.replace(/>/g, '&gt;').replace(/</g, '&lt;');
			                  // make it look nice 
			                  svggraph = "<pre class='pre-scrollable prettyprint'>" + svggraph + "</pre>";
							}

							svg.html(svggraph);
							
							// center the graph horizontally
							if (oFormat == "org.w3.svg") {
							  var svgRect = $('g#group')[0].getBoundingClientRect();
							  var offset = ($(document).width() - svgRect.width) / 2; 
							  var g = svg.svg('get').getElementById('group');
							  // we put a 1 for the y value as the IE seems to omit 0s 
							  // what breaks our regex in the draggable addon
							  g.setAttribute('transform', 'translate(' + offset + ', 1)');
							}
							
							// show graph section and hide errorDiv
							$('#resGraph').show();
							$('#errorDiv').hide();
							
							// call the prettifier
							prettyPrint();
						},
						error: function(error) {
							// hide the graph section
							$('#resGraph').hide();
							// show errorDiv
							try {
							    var errorJson = JSON.parse(error.responseText);
							    var errorPre = "<h4>Error:</h4> " + errorJson.message;
							    if (errorJson.throwable) {
							        errorPre += "<br /><br /><pre>" + errorJson.throwable + "</pre>";    
							    }
							} catch (err) {
							    var errorPre =  "<h4>Unknown Error.</h4>";
							}
							$('#errorDiv').html(errorPre);
							$('#errorDiv').show();	
						}
					});
				});
				
				// add initial example data
				var exGraph = "{\n  id: \"root\",\n  layoutOptions: { 'elk.algorithm': 'layered' },\n  children: [\n    { id: \"n1\", width: 30, height: 30 },\n    { id: \"n2\", width: 30, height: 30 },\n    { id: \"n3\", width: 30, height: 30 }\n  ],\n  edges: [\n    { id: \"e1\", sources: [ \"n1\" ], targets: [ \"n2\" ] },\n    { id: \"e2\", sources: [ \"n1\" ], targets: [ \"n3\" ] } \n  ]\n}";
				$('#srcArea').val(exGraph);
				var exConfig = "spacing: 10,\nalgorithm: org.eclipse.elk.layered,\nedgeRouting: ORTHOGONAL";
				$('#configArea').val(exConfig);
			});
			</script>
		'''
	}
	
	/**
	 * 
	 */
	override providerOverride(ResourceProcessingExchange processingExchange) {
		return false;
	}
	
}