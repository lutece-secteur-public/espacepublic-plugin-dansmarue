package fr.paris.lutece.plugins.dansmarue.service;

import fr.paris.lutece.plugins.dansmarue.business.entities.ConseilQuartier;

import java.util.List;


public interface IConseilQuartierService
{
    List<ConseilQuartier> selectQuartiersList();
}
