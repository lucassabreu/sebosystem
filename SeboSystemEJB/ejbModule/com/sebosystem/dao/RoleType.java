package com.sebosystem.dao;

import java.util.HashSet;
import java.util.Set;

public enum RoleType {
    Guest, Reader(Guest), Moderator(Reader), Adminstrator(Moderator);

    protected RoleType[] parents;
    protected Set<String> parentsSet;

    RoleType(RoleType... parents) {
        this.parents = parents;
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
}
