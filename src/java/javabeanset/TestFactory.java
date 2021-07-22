/*
 * Copyright (c) 2002-2021, City of Paris
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
package javabeanset;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.dansmarue.business.entities.FeuilleDeTournee;
import fr.paris.lutece.plugins.dansmarue.business.entities.SignalementBean;

/**
 * Classe permettant de générer une liste de feuille de tournée. Cette liste peut etre utilisée dans jaspersoft en tant que data adaptater, afin de tester le
 * template jasper.
 *
 * Dans jasperstudio, créer un data adapters (Collection of JavaBeans).
 *
 * Renseigner Factory class: javabeanset.TestFactory , méthode: genererateList.
 *
 * Ajouter les jars nécessaires (plugin-dansmarue, lutece-core...).
 *
 * Ajouter si nécessaire le jar au classpath du jrxml
 *
 */
public class TestFactory
{
    private TestFactory( )
    {

    }

    /**
     * Genere la liste.
     *
     * @return the list
     */
    public static List<FeuilleDeTournee> genererateList( )
    {
        List<FeuilleDeTournee> listFDT = new ArrayList<>( );

        List<SignalementBean> listSignalementBean = new ArrayList<>( );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );
        listSignalementBean.add( generateSignalementBean( ) );

        List<Integer> listSignalementIds = new ArrayList<>( );
        listSignalementIds.add( 1 );

        FeuilleDeTournee fdt = new FeuilleDeTournee( 1, "Toto", 1, "01/01/2020", "02/01/2020", "Ceci est un commentaire", 1, listSignalementIds, null,
                listSignalementBean );

        listFDT.add( fdt );

        return listFDT;
    }

    /**
     * Generate signalement bean.
     *
     * @return the signalement bean
     */
    private static SignalementBean generateSignalementBean( )
    {
        SignalementBean signalementBean = new SignalementBean( );
        signalementBean.setAdresse( "10 place du Bataillon français de l'ONU en corée 75004 Paris" );
        signalementBean.setCommentaire( "A proximité de la colone à verre" );
        signalementBean.setCommentaireAgentTerrain( "RAS" );
        signalementBean.setDateCreation( "12/08/2020" );
        signalementBean.setDatePrevueTraitement( "13/08/2020" );
        // Récupéré de la manière suivante: Base64.getEncoder().encodeToString( byteArray )
        signalementBean.setImageEnsembleContent(
                "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAA21BMVEUzY5D/mQD///81ZZE9a5X/mwH//vz/9+v/rzv/1Jf/7dL/58X/pBz/nQr/vF3/nwr/+/X/4rb/8No1ZI7/2qH/u1QwYpI7ZYr/qSpRa3tKaYBLdZ36mAT/t0k+Zojy9fiafkhjcG5vc2b/0o3k6vDZjxnylgrBz911lbRqja6Sq8NEcJl1dWHTjh66hzFNan6hgEKrgznkkhSvwdN8dl2Cd1ingj3HiiZXbHZocmqLpr/W4OlghajM2eOwhDbDiSiKelGSfE2afkddbnL/zIH/yHT/2Z//wmd/eFkM4E3EAAANbklEQVR4nO2d+ZuTSBPHmRw67ni8isoEMAQM5AJCCAFycQRd5///i95qmFFHTWig6Rhmv/vT+rgbPk9VV1WfxbR+kTTcjpbTzXiyE2adS9FMECbjzXQ52g4l6Rcg5jEe8O0vDxAh7jLE/W+MjwglMB/ATXY7IeVrX4pSRmEHlOPNcjQ8SigNR9PJjGHa8M8lKv3u2WQ6Gkp/JAS+5WYidM79nRXVESbIjNLvhKkBBfDMc39iRYHHCo/MyDzw7ZtgwEypGfcPjPeEqQFnF2/ATG0IO+PpQ8DJCKX9dCw0Ay9TGxD30g9Cabhsioc+qCOMl5mfMpmPbhplQaS2sMn8FBFKWRpsmFBilO4Jm+ejSBBRl8OUUEI+2pAo+rPaHeSnEiIEH22eBZE6qZ8yDfVRpNRPJUbabpqS6X8VZP7NVmKG+/G5v6RGjfdDZjuanPszatR4tGVGywYTtifLEbOc7s79HTVqN10y002DCdu7zZTZjIVzf0eNEsYbZjxpNOFkzOx2zSu6f2i2mzACzYLmYyZ6P9gRBGY2q5+QZbk+iOfnPaQ5z6N/5Vi29l/uzASm5tW11GRsH9j8ZK3ozmq1cnRlnfjAybFM3RbtdGZMu10TIctx91bzE3kNbFF8sNwgCFxrEEfAuZaBM7NobeZsdzr1EXJ8T9ZXiGoRmKYZhp5nGCqSYXheGJpmECxc6xCvdLnHc/V8RF2ELAw8jk/02A1VWxOvusd0JWq2GrqRkvAc2JG8IeshRMZTnOjgmqGBAMWr4xIRohGa7iBylKTHk2YkT4isxydKZJmGrWlAdwrvAVKEv2kbppWakqwlSROy/V4C1hu4pqdqYjcX7mdOTfUeLNknx0iWkGX5nhIHyDVt7bRz/tFdNRs5bBAr4KykGEkSgnf665UVgvEKof1mytBarX2UK0mIICHLzcF+Xho6qwhFHg/sOCeDSIoQirJ5og9CtdjYO6KuqIYDPZn3CUASIgT7gX+i6FnNfg8S08i6WhOwIxFCKDohwJiqRogvY9RUE4WcqmGVBCE3l514AQGGGF4mCDmL2JHn1cq56oSQIdYRWfs9KLVjtK6WOaoSAp/sHMxqCeIUo2oeHLkKY2VCLolDZEASIfR3dZEZwzipEHCqEbJcDzzU1urBu4fUbPDUXmnGSoQQQ3XXI5Uhjgkyh+fqpWNqBUKW7ftrq64R+JhRtdZ+v9xgrEAIc6QoMIrW16UIRc0IoqTcKkB5QpZPVmZdEeZXQcQxV0mp2XFpwr6vDEwyVSgeomoOFL9PjRCCqG55FIbgD4maZ+klQmo5QrafOJZn0zJgpq7tWU5SOKSWI+SyGEMVEFkRxZui4aYUYb/nBMTrbBxpauD0Co7FMoSsr1v0LYgEVrR0v5ifFidkOV6BIHMGPiQIN0qxFZwShLw88OxzWBBJtL1BsbxYmBDCKCT601G0++rVm0d61SUWdruaeVcooBYlhFoUprunLfjm9vrTY12/fEWKEEWbqMiKcVFCjl8HeUHm5YevXz4/0tfrN+QQRS1YF/DTYoQsC6WMkVeq3X799+3NI734RNCIXREC6hx7olGQkFu7at58t3v9+ebd80d6+4EgIRQ3qitjx9NChDCdiPKL0e71i3+eP3t08ejmNUlCVKJG2AG1GKEcm/l54nfCZ6QJRduMkxoIOd4J7fxUXz8hBFTbdPp4FWoBQq63PuCsWdAghPJtsJ5jWbEAYR98FGdVjQYhSvyRjFWD4xOyvJOX6ikSXsFMysEKNtiEUMzEeOtqdAhFTY2xZvzYhFxPX+BNKOgQdkVtoeBs2mAT9pUB5pSJDiH4aRivMUYiLiHbyy24aROKqrnCCKeYhDBncnHXLWgRwizDwtiywSTs93SsTEGV8Eoz9fyRiEfI8vrBwJ3WUyQ0BkpuxsAk9Ach9sIFPULRDuPcdSksQo5XAvxNQnqEaL6fOxnGIuz7K2wfpUooit4qb/0UhxBN7FX8n6VIeHWlWkrvtBGxCGUYhX8pYf5EEYeQUwqt4VMlhJGoVCZk+06hfTSqhKIWOqdTIgYhx0dqkQVdqoRXohGdjqb5hCyfHArEGdqEV+og4SsS+vqi0F4oZULbPR1NMQjlyCy000Sb0IxORlMMQt01/mZCtN9WjZC7w1lCPCOhap6MprmEbS4qeOyJMqGoGVEVQpbjrYLbobQJRXtwalc4j5DjE8wFqO+iTAh+6p7axMgj7Pt6sUh6DkJT6R3301zCdewV3LOnTxieWv7OI+SdoMDUMBV1QtFw9eNlTQ7hRz4qfO6CPqEarvijN23zCOeDwkeA6RPaajwvTdiztKKHg6gTXmn2oVee0C18Bpg+oahZpQkZf3F1AYSiW5qQ9YPCx5noE15dLfyjNjpNyHJJUPi81hkIu0Fy9HxNDmFfNi+DUD56Duw0Iccrl0Foro9eVThN2J/rF0Koz4/VbTmEvhNeBGF4/HB0DmGyuhDC1dGrGKcJeTnyLoLQi5KShDB3ugzCeH1sdpFDqByMiyBEm8FlCD/OdetCCC392Owih9BxC21ZZD93BkLVdY5VpqcJe6vgQgiD1X+ET5aw8eOw8bH0gvLhoVw+fAo1TePr0ubPLZ7A/LDxc/zmr9NczFqbWXatrfnrpc1f834C+xbN33t6AvuHzd8Dbvw+fvPPYlzIeZq4wnmayzgTpVc4E8Xx8t9+rq2LzrWVJ7yIs4mHKmcTmTaHef33bIQVz5c+gTPCT+CcN3oM428mtM1IrkjY/PsWjb8zcwH3nk5e7frv7lpqxKbfP6x+h7R18+325StsFSG0QxJ3SCvfA2798/X6+hZTL4uYu0vmHnDlu9ytd5+/vf6Ap0+fbt9gxzVSd7kr38dvPb95+x5P/7748volNiGx+/joTQX8xYw/ELae/Q9X724+X+MSkntTAb2LYVV4F6OAnj3/B5+Q4LsY6NVZ7OK0KuELfEJyb5tUfJ+mJkKi79Owld4YqoeQ7BtD1d6JqoeQ8DtR6K2v4G8iJP/WF9v3B2Xfa6uDkPx7bVXe3KuDEC3PkH1zL13+DnH8lAphHe8mwkhUsPp10CAEHz2Qf/sSytM7nPewaBBqduhgNvQoQsimE8USb9ASJ0Rv0J5cYCtJyDB8EuU/qF9/XVrfO8IM118v8N6Cxp5LlJhbdDXVXdfzFjQq3hw37z3v7vWXtzfvyurm7ZfbHEDRsJy63vPOzoHl+enttxfv35bV+8/fcgjTN9nx+3jU8K5+9+X1pw+vy+rDp+vTqxhQcEd+je/qo6Nueb0Rut03VXR6IQpS/er4ITYShGhX+Mz9LeR6+1ugXWH9rD1K9Lp7lKB1Kcel0W7td0Gx5jq195lJ+zY3vFdQ1u+JvhVF1J6ETr8ntKfouF6BvQwS6tqe6xTvLle+75pzhr5rDrW+a0x6WIpm7zyU6Ac6xd55WRNZyv0Py7WULU2Ypv7IpNOdTNQMM5Jp97BEzcZ9hUpiRGlQ8Uu2Ia9AmPaSdRaGXXMnUlG0jYVzjl6yzH0/4IKHwgpLs8Oz9QNm0i2bQajW11BWBA8NB8U7VxIkhHhzZ5Hvq/4g4LPu5GK1NlnCNG0ocY291WPlvL3VmXQwys4g8IjbUVO9YODI5YcgKUK0BMenFQ7J4YgGIKpieMzGVfUSphMqJXJNg1hY1WzDdCOlSKPDeglR+u+lPYLJWFG0YS6f9Eom+VoImdSO+iAw1Krd5EXNVo1goBOwHxJJQihxfGWVpY6yBXkXrWiH1kqBAVgphH4XQUImzRwQciCsgiGLpw9IDmA+FEAhwJDBY0gTorDakxUnOixQ1CmGKKLosjhEjiL3qgbQn0SaENkRkkeix26IDCmKOBkE/hIyX+hGegLpgZj9kMgTMmkp58v6XWwtTM9IMY8Py24KZ3jmworudNmvUqD9UbUQIkOCKaFkdeLAQ97aPS7knV4Qg/E4Dv1npD+lJsLs/w2mXDvRwHIXQWCaZhh6nmGoSIbheWEIfxYEC9caRM4ajFfTV9RIiMqAPs/Pez3fT2RFd1ZRfLDcIAhc6xBHK0dX5MT3e705z/dJJPc/ChF2OjURpvoIQqz8HDDXiHOF2NYAN0dRJfsL9anTmTGzWafGX8iEIiwyKLIoGA2sBmYjHDX/rM5MYAShfsLv+piJ3g92hB2z283o/SB1zXYTZjwRzv0ZNUqYjJnNuNGE4w0z3ezO/Rk1areZMstpowmnS2a0nJz7M+pTe7IcMfsmEzKT0ZYZ7sfn/owaNd4PGWm7mdVauJ1P7c5ss5WY1nA5oVnWUFRHmCyHLaYljaaThhJOpiMJEW5HG6GBftruCJvRFhG2pGb6aeqjUgsRpn7avPJ7lvpoRtgagp82zU3b4KPD1gNhA/30wUfvCVvSfjpulBXbwni6TwHvCcFPpxOhKZkfMr0Ag3DY+plQGu6Xm6Z4KnjoZrnPfPQ7IWJEZuzUtbZITe1OJzWg9AD2nRAhNsGMqQF/AvyJ8N6Ml54YZ48M+AshKuCWm/FkstsJEHbAYy9GHQguwm43mYyRAR9dpnpE2JKk4Xa/XE4Bc4dC66UI+HYAN12O9tuhJJ0gzKLqCCFeHGEKuB1KvwL9H6BgsHKVRDP0AAAAAElFTkSuQmCC" );
        signalementBean.setImagePresContent(
                "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" );
        signalementBean.setNumeroComplet( "A2020G59" );
        signalementBean.setPriorite( "Genant" );
        signalementBean.setTypeSignalementComplet( "Mobiliers urbains > Protection > Potelet, barrière ou garde-corps détérioré" );
        signalementBean.setStatut( "A faire terrain" );

        return signalementBean;
    }
}
