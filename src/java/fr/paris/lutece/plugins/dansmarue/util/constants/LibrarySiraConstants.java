/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dansmarue.util.constants;

/**
 * SignalementConstants.
 */
public final class LibrarySiraConstants
{

    /** The Constant PROPERTY_REST_RAMEN_URL. */
    // WebService links
    public static final String PROPERTY_REST_RAMEN_URL                           = "ramen-rest-url";

    /** The Constant PROPERTY_REST_DMR_URL. */
    public static final String PROPERTY_REST_DMR_URL                             = "dmr-rest-url";

    /** The Constant REST_SAVE_DOSSIER. */
    public static final String REST_SAVE_DOSSIER                                 = "saveDossier";

    /** The Constant REST_SAVE_DOSSIER_HISTORY. */
    public static final String REST_SAVE_DOSSIER_HISTORY                         = "saveDossierHistory";

    /** The Constant REST_GET_DOSSIER. */
    public static final String REST_GET_DOSSIER                                  = "getDossier";

    /** The Constant REST_GET_DOSSIER_COURRANT_BY_GEOM_WITH_EXPIRATION. */
    public static final String REST_GET_DOSSIER_COURRANT_BY_GEOM_WITH_EXPIRATION = "getDossiersCourrantsByGeomWithExpiration";

    /** The Constant REST_GET_DOSSIER_COURRANT_BY_GEO_WITH_LIMIT. */
    public static final String REST_GET_DOSSIER_COURRANT_BY_GEO_WITH_LIMIT       = "getDossiersCourrantsByGeomWithLimit";

    /** The Constant REST_GET_SERVICE_BY_ID. */
    public static final String REST_GET_SERVICE_BY_ID                            = "getServiceById";

    /** The Constant REST_GET_ALL_TYPE_OBJECT_ACTIFS. */
    public static final String REST_GET_ALL_TYPE_OBJECT_ACTIFS                   = "getAllTypeObjetActifs";

    /** The Constant REST_NATURE_BY_TYPE_OBJECT. */
    public static final String REST_NATURE_BY_TYPE_OBJECT                        = "getNatureByTypeObjetId";

    /** The Constant REST_TYPE_SIGNALEMENT_SERVICE. */
    public static final String REST_TYPE_SIGNALEMENT_SERVICE                     = "getAllSousTypeSignalementCascade";

    /** The Constant REST_LIST_SIGNALEMENT_DOSSIER_SERVICE. */
    public static final String REST_LIST_SIGNALEMENT_DOSSIER_SERVICE             = "getListSignalementDossierByParam";

    /** The Constant REST_GET_SIGNALEMENT_BY_ID. */
    public static final String REST_GET_SIGNALEMENT_BY_ID                        = "getSignalementById";

    /** The Constant REST_GET_LIST_WORKFLOW_ACTION. */
    public static final String REST_GET_LIST_WORKFLOW_ACTION                     = "getWorkflowActions";

    /** The Constant REST_HAS_SIGNALEMENT_FOR_ROADMAP. */
    public static final String REST_HAS_SIGNALEMENT_FOR_ROADMAP                  = "hasSignalementForRoadMap";

    /**
     * Utility class - empty constructor.
     */
    private LibrarySiraConstants( )
    {
        // nothing
    }

}
