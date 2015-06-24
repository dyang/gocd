package com.thoughtworks.go.config.remote;

import com.thoughtworks.go.config.CaseInsensitiveString;
import com.thoughtworks.go.config.Validatable;
import com.thoughtworks.go.config.ValidationContext;
import com.thoughtworks.go.config.materials.AbstractMaterialConfig;
import com.thoughtworks.go.domain.BaseCollection;
import com.thoughtworks.go.domain.ConfigErrors;
import com.thoughtworks.go.domain.materials.MaterialConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * List of remote configuration sources and how to interpret them
 */
public class ConfigReposConfig extends BaseCollection<ConfigRepoConfig> implements Validatable {

    private  ConfigErrors errors = new ConfigErrors();

    public ConfigReposConfig(ConfigRepoConfig... configRepos)
    {
        for(ConfigRepoConfig repo : configRepos)
        {
            this.add(repo);
        }
    }

    public boolean hasMaterial(MaterialConfig materialConfig) {
        for(ConfigRepoConfig c : this)
        {
            if(c.getMaterialConfig().equals(materialConfig))
                return  true;
        }
        return  false;
    }

    @Override
    public void validate(ValidationContext validationContext) {
        this.validateMaterialUniqueness();
    }
    private void validateMaterialUniqueness() {
        Map<String, ConfigRepoConfig> materialHashMap = new HashMap<String, ConfigRepoConfig>();
        for (ConfigRepoConfig material : this) {
            material.validateMaterialUniqueness(materialHashMap);
        }
    }

    @Override
    public ConfigErrors errors() {
        return errors;
    }

    @Override
    public void addError(String fieldName, String message) {
        this.errors().add(fieldName,message);
    }

    public boolean isReferenceAllowed(ConfigOrigin from, ConfigOrigin to) {

        if(isLocal(from) && !isLocal(to))
            return false;
        return true;
    }

    private boolean isLocal(ConfigOrigin from) {
        // we assume that configuration is local (from file or from UI) when origin is not specified
        if(from == null)
            return true;
        return from.isLocal();
    }
}