/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2011 by
 * + Kiel University
 *     + Department of Computer Science
 *         + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.kwebs.server.web

import de.cau.cs.kieler.kwebs.server.configuration.Configuration

/**
 * This class implements a web content provider for displaying the service meta data in HTML format.
 *
 * @author  swe
 */
class IndexProvider extends AbstractProvider {
    /**
     * 
     */
    override CharSequence getHeaders(ResourceProcessingExchange processingExchange) {
        return '''
            ««« Override bootstrap with original
            «««<link rel='stylesheet' type='text/css' href='styles/styles.css'/>
            <script src="scripts/jquery.event.drag.js"></script>
            <script src="scripts/jquery.mousewheel.js"></script>
            <script src="scripts/jquery.svg.js"></script>
            <script src="scripts/jquery.svg.extras.js"></script>
        '''
    }

    /**
     * 
     */
    override CharSequence getBody(ResourceProcessingExchange processingExchange) {
        val Configuration config = Configuration::INSTANCE
       
       '''
            <div class="jumbotron">
              <h2>The KIELER Web Service for Layout...</h2>
              <p>
                ... provides you with easy access to an extensive 
                layout infrastructure from different programming languages.
              </p>  
              <p>
                It supports a wide range of different <em>layout algorithms</em> and <em>graph formats</em>. 
              </p>
              <p><a class="btn btn-primary btn-lg" href="Providedlayout.html" role="button">Overview</a></p>
            </div>
            
            <div class="col-md-8 col-md-offset-2" style="text-align: center;">
                <h2>Start using KIELER Layout today!</h2>
                
                <dl class="dl-horizontal" style="text-align: left;">
                  <dt>1.</dt><dd>
                    Either <a href="http://rtsys.informatik.uni-kiel.de/confluence/x/nQEF">setup</a> your own server or use <a href="http://layout.rtsys.informatik.uni-kiel.de:9444">ours</a>.
                  </dd>
                  <dt>2.</dt><dd>
                    Get the your favourite language's <a href="http://rtsys.informatik.uni-kiel.de/confluence/x/FgKE">binding</a>.
                  </dd>
                  <dt>3.</dt><dd>
                    Choose a <a href="ProvidedLayout.html#formats">graph format</a>.
                  </dd>
                  <dt>4.</dt><dd>
                    Start layouting as shown below.
                  </dd>
                  <dt>5.</dt><dd>
                    Get further information in our <a href="http://rtsys.informatik.uni-kiel.de/confluence/x/nQEF">Wiki</a>.
                  </dd>
                </dl>
           
            
                <h3>jQuery</h3>
                <pre class="prettyprint lang-javascript" style="text-align: left;">
                $.kielerLayout({graph: graph, options: options,
                    iFormat: 'org.json', oFormat: 'org.json',
                    success : function (data) {
                        console.log(data);
                    }
                });</pre>
                
                <h3>Java</h3>
                <pre class="prettyprint lang-java" style="text-align: left;">
                String layouted = KIELERLayout.layout(server, "org.json", "org.json", options, graph);</pre>
                
                <h3>C#</h3>
                <pre class="prettyprint lang-cs" style="text-align: left;">
                String layouted = KIELER.KIELERLayout.layout(server, "org.json", "org.json", options, graph);</pre>
                
                <h3>Python</h3>
                <pre class="prettyprint lang-py" style="text-align: left;">
                layouted = KIELERLayout().layout(server, "org.json", "org.w3.svg", options, graph)</pre>
            
            
                «if (config.hasConfigProperty(Configuration::FRONTEND_SERVICE_HOSTER_LOGO)) // src has to be declared relative to the classpath
                '''
                <p>
                    <div align='center'>
                        <img src='«config.getConfigProperty(Configuration::FRONTEND_SERVICE_HOSTER_LOGO)»'/> 
                    </div>
                </p>
                '''»
            
            </div>
       '''
       +
        /*'''
            <p class='title'>Welcome</p>
            <p>
                ... to the web frontend of the layout server hosted by:
            </p>
            <p>
                <div align='center'>
                    «config.getConfigProperty(Configuration::FRONTEND_SERVICE_HOSTER)»
                </div>
            </p>
            <p>
                This website is generated by the KIELER layout web server and offers information on the capabilities of the layout service,
                including configuration options and service interface details.
                We hope that the layout service will be a useful enhancement for your projects.
            </p>
            <p>
                Have fun exploring the service!
            </p>
            ''' */
        '''
            
        '''
    }

    /**
     * 
     */
    override boolean providerOverride(ResourceProcessingExchange processingExchange) {
        return false
    }

}
