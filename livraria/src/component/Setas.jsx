import React, { useState } from 'react';

const Setas = ({scrollRef}) => {
    const [scrollPosition, setScrollPosition] = useState(0)

    const handleScrollLeft = () => {
        const container = scrollRef.current;
        const newScrollPosition = scrollPosition - 200; 
        setScrollPosition(Math.max(newScrollPosition, 0));
        container.scrollTo({
            left: newScrollPosition,
            behavior: "smooth"
        });
    };

    const handleScrollRight = () => {
        const container = scrollRef.current;
        const newScrollPosition = scrollPosition + 200;
        setScrollPosition(newScrollPosition);
        container.scrollTo({
            left: newScrollPosition,
            behavior: "smooth"
        });
    };

    return (
        <div>
            <div className="ScrollButton LeftScrollButton" onClick={handleScrollLeft}>{'<'}</div>
            <div className="ScrollButton RightScrollButton" onClick={handleScrollRight}>{'>'}</div>
        </div>
    );
};

export default Setas;
