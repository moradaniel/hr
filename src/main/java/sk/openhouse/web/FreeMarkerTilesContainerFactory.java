/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.openhouse.web;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.compat.definition.digester.CompatibilityDigesterDefinitionsReader;
import org.apache.tiles.context.ChainedTilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.pattern.DefinitionPatternMatcherFactory;
import org.apache.tiles.definition.pattern.PatternDefinitionResolver;
import org.apache.tiles.definition.pattern.PrefixedPatternDefinitionResolver;
import org.apache.tiles.definition.pattern.regexp.RegexpDefinitionPatternMatcherFactory;
import org.apache.tiles.definition.pattern.wildcard.WildcardDefinitionPatternMatcherFactory;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.el.JspExpressionFactoryFactory;
import org.apache.tiles.el.TilesContextBeanELResolver;
import org.apache.tiles.el.TilesContextELResolver;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactoryException;
import org.apache.tiles.freemarker.context.FreeMarkerTilesRequestContextFactory;
import org.apache.tiles.freemarker.renderer.FreeMarkerAttributeRenderer;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.mvel.MVELAttributeEvaluator;
import org.apache.tiles.mvel.TilesContextBeanVariableResolverFactory;
import org.apache.tiles.mvel.TilesContextVariableResolverFactory;
import org.apache.tiles.ognl.ApplicationScopeNestedObjectExtractor;
import org.apache.tiles.ognl.DelegatePropertyAccessor;
import org.apache.tiles.ognl.NestedObjectDelegatePropertyAccessor;
import org.apache.tiles.ognl.OGNLAttributeEvaluator;
import org.apache.tiles.ognl.PropertyAccessorDelegateFactory;
import org.apache.tiles.ognl.RequestScopeNestedObjectExtractor;
import org.apache.tiles.ognl.SessionScopeNestedObjectExtractor;
import org.apache.tiles.ognl.TilesApplicationContextNestedObjectExtractor;
import org.apache.tiles.ognl.TilesContextPropertyAccessorDelegateFactory;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.renderer.impl.BasicRendererFactory;
import org.apache.tiles.renderer.impl.ChainedDelegateAttributeRenderer;
import org.mvel2.integration.VariableResolverFactory;

/**
 * This is a copy of CompleteAutoloadTilesContainerFactory but freemarker
 * was removed and added definitions list.
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class FreeMarkerTilesContainerFactory extends BasicTilesContainerFactory {

    /**
     * The velocity renderer name.
     */
    private static final String FREEMARKER_RENDERER_NAME = "freemarker";

    /* definitions */
    private List<String> definitions = new ArrayList<String>();

    /* paths to properties and toolbox */
    private String velocityProperties;
    private String velocityToolbox;

    /** @param definitions - tiles.xml, if not set default is in WEB-INF */
    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    /**
     * @param velocityToolbox path to the velocity properties file
     * (ex: /WEB-INF/velocity.properties
     */
    public void setVeolocityProperties(String velocityProperties) {
        this.velocityProperties = velocityProperties;
    }

    /**
     * @param velocityToolbox path to the velocity toolbox xml file
     * (ex: WEB-INF/tools.xml)
     */
    public void setVeolocityToolbox(String velocityToolbox) {
        this.velocityToolbox = velocityToolbox;
    }

    /** {@inheritDoc}  */
    @Override
    protected BasicTilesContainer instantiateContainer(
            TilesApplicationContext applicationContext) {

        return new CachingTilesContainer();
    }

    /** {@inheritDoc}  */
    @Override
    protected List<TilesRequestContextFactory> getTilesRequestContextFactoriesToBeChained(
            ChainedTilesRequestContextFactory parent) {

        List<TilesRequestContextFactory> factories =
                super.getTilesRequestContextFactoriesToBeChained(parent);

        /* register freemarker */
        registerRequestContextFactory(
        		FreeMarkerTilesRequestContextFactory.class.getName(),
                factories, parent);

        return factories;
    }


    /** {@inheritDoc}  */
   /* @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {

        super.registerAttributeRenderers(rendererFactory, applicationContext,
                contextFactory, container, attributeEvaluatorFactory);

        VelocityAttributeRenderer velocityRenderer = new VelocityAttributeRenderer();
        velocityRenderer.setApplicationContext(applicationContext);
        velocityRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        velocityRenderer.setRequestContextFactory(contextFactory);

        if (velocityToolbox != null) {
            velocityRenderer.setParameter("org.apache.velocity.toolbox",
                    velocityToolbox);
        }
        if (velocityProperties != null) {
            velocityRenderer.setParameter("org.apache.velocity.properties",
                    velocityProperties);
        }

        velocityRenderer.commit();
        rendererFactory.registerRenderer(VELOCITY_RENDERER_NAME, velocityRenderer);
    }*/
    
    @Override
    protected void registerAttributeRenderers(
            BasicRendererFactory rendererFactory, TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container, AttributeEvaluatorFactory attributeEvaluatorFactory) {
    	
        super.registerAttributeRenderers(rendererFactory, applicationContext, contextFactory,
                container, attributeEvaluatorFactory);
        FreeMarkerAttributeRenderer freemarkerRenderer = new FreeMarkerAttributeRenderer();
        freemarkerRenderer.setApplicationContext(applicationContext);
        freemarkerRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        freemarkerRenderer.setRequestContextFactory(contextFactory);
        freemarkerRenderer.setParameter("TemplatePath", "/");
        freemarkerRenderer.setParameter("NoCache", "true");
        freemarkerRenderer.setParameter("ContentType", "text/html");
        freemarkerRenderer.setParameter("template_update_delay", "0");
        freemarkerRenderer.setParameter("default_encoding", "ISO-8859-1");
        freemarkerRenderer.setParameter("number_format", "0.##########");
        freemarkerRenderer.commit();
        rendererFactory.registerRenderer("freemarker", freemarkerRenderer);
    }

    /** {@inheritDoc}  */
    @Override
    protected AttributeRenderer createDefaultAttributeRenderer(
            BasicRendererFactory rendererFactory,
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory,
            TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {

        ChainedDelegateAttributeRenderer retValue = new ChainedDelegateAttributeRenderer();
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(DEFINITION_RENDERER_NAME));
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(FREEMARKER_RENDERER_NAME));
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(TEMPLATE_RENDERER_NAME));
        retValue.addAttributeRenderer((TypeDetectingAttributeRenderer) rendererFactory
                .getRenderer(STRING_RENDERER_NAME));
        retValue.setApplicationContext(applicationContext);
        retValue.setRequestContextFactory(contextFactory);
        retValue.setAttributeEvaluatorFactory(attributeEvaluatorFactory);

        return retValue;
    }

    /** {@inheritDoc}  */
    @Override
    protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(
            TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
        BasicAttributeEvaluatorFactory attributeEvaluatorFactory =
                new BasicAttributeEvaluatorFactory(
                createELEvaluator(applicationContext));
        attributeEvaluatorFactory.registerAttributeEvaluator("MVEL",
                createMVELEvaluator());
        attributeEvaluatorFactory.registerAttributeEvaluator("OGNL",
                createOGNLEvaluator());

        return attributeEvaluatorFactory;
    }


    /** {@inheritDoc} */
    @Override
    protected <T> PatternDefinitionResolver<T> createPatternDefinitionResolver(
            Class<T> customizationKeyClass) {

        DefinitionPatternMatcherFactory wildcardFactory =
                new WildcardDefinitionPatternMatcherFactory();
        DefinitionPatternMatcherFactory regexpFactory =
                new RegexpDefinitionPatternMatcherFactory();
        PrefixedPatternDefinitionResolver<T> resolver =
                new PrefixedPatternDefinitionResolver<T>();
        resolver.registerDefinitionPatternMatcherFactory("WILDCARD", wildcardFactory);
        resolver.registerDefinitionPatternMatcherFactory("REGEXP", regexpFactory);

        return resolver;
    }

    /** {@inheritDoc} */
    @Override
    protected List<URL> getSourceURLs(TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {

        List<URL> urls = new ArrayList<URL>();

        try {
            /* no definitions, load default */
            if (definitions.isEmpty()) {
                Set<URL> webINFSet = applicationContext.getResources("/WEB-INF/**/tiles*.xml");
                Set<URL> metaINFSet = applicationContext.getResources("classpath*:META-INF/**/tiles*.xml");

                createResourceUrl(applicationContext, urls, webINFSet);
                createResourceUrl(applicationContext, urls, metaINFSet);
            } else {
                for (String def : definitions) {
                    if (def.indexOf("*") > -1) {
                        // this is a wildcard definition
                        Set<URL> urlSet = applicationContext.getResources(def);
                        createResourceUrl(applicationContext, urls, urlSet);
                    } else {
                        urls.add(applicationContext.getResource(def));
                    }
                }
            }
        } catch (IOException e) {
            throw new DefinitionsFactoryException(
                    "Cannot load definition URLs", e);
        }

        return urls;
    }

    private void createResourceUrl(TilesApplicationContext applicationContext,
            List<URL> urls, Set<URL> urlSet) throws IOException {
        for (URL url : urlSet) {
            String externalForm = url.toExternalForm();

            if (externalForm.indexOf('_', externalForm.lastIndexOf("/")) < 0) {
                urls.add(url);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext,
            TilesRequestContextFactory contextFactory) {
        return new CompatibilityDigesterDefinitionsReader();
    }

    /**
     * Creates the EL evaluator.
     *
     * @param applicationContext The Tiles application context.
     * @return The EL evaluator.
     */
    private ELAttributeEvaluator createELEvaluator(
            TilesApplicationContext applicationContext) {
        ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
        evaluator.setApplicationContext(applicationContext);
        JspExpressionFactoryFactory efFactory = new JspExpressionFactoryFactory();
        efFactory.setApplicationContext(applicationContext);
        evaluator.setExpressionFactory(efFactory.getExpressionFactory());
        ELResolver elResolver = new CompositeELResolver() {
            {
                add(new TilesContextELResolver());
                add(new TilesContextBeanELResolver());
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
                add(new BeanELResolver(false));
            }
        };
        evaluator.setResolver(elResolver);
        return evaluator;
    }

    /**
     * Creates the MVEL evaluator.
     *
     * @return The MVEL evaluator.
     */
    private MVELAttributeEvaluator createMVELEvaluator() {
        TilesRequestContextHolder requestHolder = new TilesRequestContextHolder();
        VariableResolverFactory variableResolverFactory = new TilesContextVariableResolverFactory(
                requestHolder);
        variableResolverFactory
                .setNextFactory(new TilesContextBeanVariableResolverFactory(
                        requestHolder));
        MVELAttributeEvaluator mvelEvaluator = new MVELAttributeEvaluator(requestHolder,
                variableResolverFactory);
        return mvelEvaluator;
    }

    /**
     * Creates the OGNL evaluator.
     *
     * @return The OGNL evaluator.
     */
    private OGNLAttributeEvaluator createOGNLEvaluator() {
        try {
            PropertyAccessor objectPropertyAccessor = OgnlRuntime.getPropertyAccessor(Object.class);
            PropertyAccessor mapPropertyAccessor = OgnlRuntime.getPropertyAccessor(Map.class);
            PropertyAccessor applicationContextPropertyAccessor =
                new NestedObjectDelegatePropertyAccessor<TilesRequestContext>(
                    new TilesApplicationContextNestedObjectExtractor(),
                    objectPropertyAccessor);
            PropertyAccessor requestScopePropertyAccessor =
                new NestedObjectDelegatePropertyAccessor<TilesRequestContext>(
                    new RequestScopeNestedObjectExtractor(), mapPropertyAccessor);
            PropertyAccessor sessionScopePropertyAccessor =
                new NestedObjectDelegatePropertyAccessor<TilesRequestContext>(
                    new SessionScopeNestedObjectExtractor(), mapPropertyAccessor);
            PropertyAccessor applicationScopePropertyAccessor =
                new NestedObjectDelegatePropertyAccessor<TilesRequestContext>(
                    new ApplicationScopeNestedObjectExtractor(), mapPropertyAccessor);
            PropertyAccessorDelegateFactory<TilesRequestContext> factory =
                new TilesContextPropertyAccessorDelegateFactory(
                    objectPropertyAccessor, applicationContextPropertyAccessor,
                    requestScopePropertyAccessor, sessionScopePropertyAccessor,
                    applicationScopePropertyAccessor);
            PropertyAccessor tilesRequestAccessor = new DelegatePropertyAccessor<TilesRequestContext>(factory);
            OgnlRuntime.setPropertyAccessor(TilesRequestContext.class, tilesRequestAccessor);
            return new OGNLAttributeEvaluator();
        } catch (OgnlException e) {
            throw new TilesContainerFactoryException(
                    "Cannot initialize OGNL evaluator", e);
        }
    }
}
