package hu.rivalsnetwork.rivalsapi;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class Bootstrap implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("dev.dejvokep:boosted-yaml:1.3"), null));

        classpathBuilder.addLibrary(resolver);
    }
}