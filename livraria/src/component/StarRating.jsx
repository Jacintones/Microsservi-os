import "./Css/StarRating.css"

const StarRating = ({ rating, numAvaliacoes }) => {
  const starsArray = []
  for (let i = 1; i <= 5; i++) {
      starsArray.push(i)
  }
    return (
        <div className="star-rating-container"> 
            {starsArray.map(star => (
                <span key={star} role="img" aria-label="star">{star <= rating ? "⭐️" : "☆"}</span>
            ))}
            <p>({numAvaliacoes})</p>
        </div>
    )
}

export default StarRating
