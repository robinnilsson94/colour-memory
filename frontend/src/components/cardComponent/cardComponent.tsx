import React from "react";
import type {Card} from "../../models/card";
import "./CardComponent.css";

interface CardComponentProps {
    card: Card;
    isFlipped: boolean;
    onClick: () => void;
}

const CardComponent: React.FC<CardComponentProps> = ({card, isFlipped, onClick}) => {
    return (
        <div
            className={`card ${isFlipped ? card.color.toLowerCase() : "back"}`}
            onClick={onClick}
        />
    );
};

export default CardComponent;
