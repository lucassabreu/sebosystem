package com.sebosystem.dao;

import java.util.HashSet;
import java.util.Set;

import com.sebosystem.dao.helper.EnumTypeKey;

public enum RoleType implements EnumTypeKey {
    Reader, Moderator(Reader), Administrator(Moderator);

    protected String key;

    protected RoleType[] parents;
    protected Set<String> parentsSet;

    RoleType(RoleType... parents) {
        this.parents = parents;
        this.key = this.name().toLowerCase();
    }

    public Set<String> getParentsSet() {
        if (this.parentsSet == null) {
            this.parentsSet = new HashSet<>();

            Set<RoleType> parentsSet = new HashSet<>();
            this.feedParentsSet(parentsSet);

            for (RoleType role : parentsSet) {
                this.parentsSet.add(role.name().toLowerCase());
            }
        }

        return this.parentsSet;
    }

    public RoleType[] getParents() {
        return parents;
    }

    protected void feedParentsSet(Set<RoleType> parents) {
        parents.add(this);
        for (RoleType role : this.parents) {
            if (!parents.contains(role))
                role.feedParentsSet(parents);
        }
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
